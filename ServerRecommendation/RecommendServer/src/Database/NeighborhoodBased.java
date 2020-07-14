package Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class NeighborhoodBased {

	private final int NUM_USER_DEFAULT = 1000; //5;//944
	private final int NUM_ITEM_DEFAULT = 1000; //8;//1683
	private final int K_NEAREST_NEIGHBOR = 10;
	private boolean NEED_EVALUATE = false;
	
	private HashMap<Integer, RowInfo> userRatingInfo = new HashMap<>();
	private float[][] utilityMatrix;
	private float[][] normalizedMatrix;
	private double[][] similarityMatrix;
	private int numUser = 0;
	private int numItem = 0;
	
	//Evaluate
	private float[][] predictMatrix;
	private float[][] testMatrix;
	
	
	public NeighborhoodBased()
	{
		this.numUser = NUM_USER_DEFAULT;
		this.numItem = NUM_ITEM_DEFAULT;
		utilityMatrix = new float[NUM_USER_DEFAULT][NUM_ITEM_DEFAULT];
		normalizedMatrix = new float[NUM_USER_DEFAULT][NUM_ITEM_DEFAULT];
		similarityMatrix = new double[NUM_USER_DEFAULT][NUM_USER_DEFAULT];
		if (NEED_EVALUATE) 
		{
			predictMatrix = new float[NUM_USER_DEFAULT][NUM_ITEM_DEFAULT];
			testMatrix = new float[NUM_USER_DEFAULT][NUM_ITEM_DEFAULT];
		}
			
	}
	
	public NeighborhoodBased(int numUser, int numItem)
	{
		this.numUser = numUser;
		this.numItem = numItem;
		utilityMatrix = new float[numUser][numItem];
		normalizedMatrix = new float[numUser][numItem];
		similarityMatrix = new double[numUser][numUser];
		if (NEED_EVALUATE) 
		{
			predictMatrix = new float[numUser][numItem];
			testMatrix = new float[numUser][numItem];
		}
	}
	
	
	public void setUtilityMatrix(float[][] UtilityMatrix)
	{
		this.utilityMatrix = UtilityMatrix;
	}
	
	public float[][] getTestMatrix() {
		return testMatrix;
	}

	public void setTestMatrix(float[][] testMatrix) {
		this.testMatrix = testMatrix;
	}

	public void normalizedMatrix()
	{
		if (this.userRatingInfo == null || this.userRatingInfo.size() == 0)
		{
			System.out.println("There are no rating in utilitymatrix, normalized ended with nothing done");
			return;
		}
		for (int i=1; i< this.numUser; i++)
		{
			if (this.userRatingInfo.containsKey(i))
			{
				float averageValue = (this.userRatingInfo.get(i).sum / this.userRatingInfo.get(i).numValue);
				//System.out.println("Average value: " + averageValue);
				for (int j=1; j< this.numItem; j++)
				{
					if (this.utilityMatrix[i][j] != 0)
					normalizedMatrix[i][j] = this.utilityMatrix[i][j] - averageValue;
				}
			}
		}
	}
	
	public void calculateSimilarityMatrix()
	{
		for (int i=1; i< this.numUser; i++)
		{
			
			for (int j= i; j< this.numUser ; j++)
			{
				//System.out.println("Calculated user: " + i + "\t " + j);
				double similarity = this.similarity(i, j);
				this.similarityMatrix[i][j] = similarity;
				this.similarityMatrix[j][i] = similarity;
			}
		}
	}
	

	public HashMap<Integer, RowInfo> getUserRatingInfo() {
		return userRatingInfo;
	}

	public void setUserRatingInfo(HashMap<Integer, RowInfo> userRatingInfo) {
		this.userRatingInfo = userRatingInfo;
	}

	public float[][] getNormalizedMatrix() {
		return normalizedMatrix;
	}

	public void setNormalizedMatrix(float[][] normalizedMatrix) {
		this.normalizedMatrix = normalizedMatrix;
	}

	public double[][] getSimilarityMatrix() {
		return similarityMatrix;
	}

	public void setSimilarityMatrix(double[][] similarityMatrix) {
		this.similarityMatrix = similarityMatrix;
	}

	public float[][] getUtilityMatrix() {
		return utilityMatrix;
	}
	
	public double similarity(int userA, int userB)
	{
		if (userA == userB) return 1;
		return similarityTwoArr(this.normalizedMatrix[userA],this.normalizedMatrix[userB], this.numItem);
	}
	
	private double similarityTwoArr(float[] arrA, float[] arrB, int size)
	{
		double sim = 0;
		float sumAxB = 0;
		float sumPowA = 0;
		float sumPowB = 0;
		for (int i = 1; i < size; i++)
		{
			sumAxB += arrA[i] * arrB[i];
			sumPowA += Math.pow(arrA[i], 2);
			sumPowB += Math.pow(arrB[i], 2);
		}
		sim =  sumAxB / (Math.sqrt(sumPowA) * Math.sqrt(sumPowB) +0.000000001f);

		return sim;
	}
	
	public void printUtilityMatrix()
	{
		for (int i= 1; i< this.numUser; i++)
	      {
			if (!this.userRatingInfo.containsKey(i))
				continue;
	    	  for (int j= 1; j< this.numItem; j++)
	    		 // if (normalizedMatrix[i][j] != 0) 
	    		  System.out.print(utilityMatrix[i][j] + "\t");
	    	  System.out.println();
	      }
	}
	
	public void printInfo()
	{
		 for (Map.Entry<Integer, RowInfo> entry: userRatingInfo.entrySet())
	      {
	    	  System.out.println("user ID:" + entry.getKey() +"\t sum= " + entry.getValue().sum + "\t numValue = " + entry.getValue().numValue );  
	      }
	}
	
	public void printNormalizedMatrix()
	{
		for (int i=1; i< this.numUser; i++)
	      {
			if (!this.userRatingInfo.containsKey(i))
				continue;
	    	  for (int j=1; j< this.numItem; j++)
	    		 // if (normalizedMatrix[i][j] != 0) 
	    			  System.out.print(normalizedMatrix[i][j] + "\t");
	    	  System.out.println();
	      }
	}
	
	public void printSimilarityMatrix()
	{
		for (int i=1; i< this.numUser; i++)
	      {
			if (!this.userRatingInfo.containsKey(i))
				continue;
	    	  for (int j=1; j< this.numUser; j++)
	    		 // if (normalizedMatrix[i][j] != 0) 
	    			  System.out.print(this.similarityMatrix[i][j] + "\t");
	    	  System.out.println();
	      }
	}
	
	public ArrayList<RateInfo> predictRateUserItem(int userId, int itemId)
	{
		ArrayList<RateInfo>  rateInfo = new ArrayList<>();
		for (int i = 1; i< this.numUser; i++)
		{
			if (this.utilityMatrix[i][itemId] != 0 && userId != i)
			{
				rateInfo.add(new RateInfo(i, this.similarityMatrix[userId][i],this.utilityMatrix[i][itemId]));
			}
		}
			
		/*
		for (RateInfo ri : rateInfo)
		{
			System.out.println("user " + ri.userId 
			+ "\trate item " + itemId + "\tvalue= " + ri.rate 
			+ "\tsimilarity = " + ri.sim);
		}
		*/
		return rateInfo;
	}
	
	public ArrayList<Rate> predictItemsForUser(int userId, int numItem)
	{
		ArrayList<RateInfo>  rateInfo = new ArrayList<>();
		
		//get ListUserId with similarity descending
		for (int i = 1; i< this.numUser; i++)
		{
			if (i == userId)
				continue;
			//System.out.println("userID = " + i + "sim = " + this.similarityMatrix[userId][i]);
			rateInfo.add(new RateInfo(i, this.similarityMatrix[userId][i], 0));
		}
		
		sortDescendingBySim(rateInfo);	
		
		
		/*for (RateInfo ri : rateInfo)
		{
			System.out.println("userID = " + ri.userId + "sim = " + ri.sim + "\trate = " + ri.rate);
		}*/
		
		ArrayList<Rate> predictResult = new ArrayList<>();
		for (int i = 1; i< this.numItem; i++)
		
		if (NEED_EVALUATE || this.utilityMatrix[userId][i] == 0)
		{
			ArrayList<RateInfo> listFiltered = filterListRateInfoWithItemId(rateInfo, i);
			
			//System.out.println("predict rate user: " + userId +" \titemId: " + i + " = "+ predictRateUserToItem(listFiltered));
			float rate = 0;
			if (listFiltered.size() > 0) 
			{ 
				rate = predictRateUserToItem(listFiltered);
				predictResult.add(new Rate(i, rate));	
			}	
		}
		sortDescendingByRate(predictResult);
		/*
		for (Rate r: predictResult)
		{
			System.out.println("itemID = " + r.itemId + " rate = " + r.rate);
		}*/
		return predictResult;
	}
	
	public void sortDescendingBySim(ArrayList<RateInfo> listToSort)
	{
		Collections.sort(listToSort, new Comparator<RateInfo>() {
	        @Override
	        public int compare(RateInfo ri1, RateInfo ri2)
	        {
	        	if (ri1.sim == ri2.sim) return 0;
	            if (ri1.sim < ri2.sim) return 1;
	            return -1;
	        }
	    });
	}
	
	public void sortDescendingByRate(ArrayList<Rate> listToSort)
	{
		Collections.sort(listToSort, new Comparator<Rate>() {
	        @Override
	        public int compare(Rate ri1, Rate ri2)
	        {
	        	if (ri1.rate == ri2.rate) return 0;
	            if (ri1.rate < ri2.rate) return 1;
	            return -1;
	        }
	    });
	}
	
	public ArrayList<RateInfo> filterListRateInfoWithItemId(ArrayList<RateInfo> listNeedFilter,int itemId)
	{
		ArrayList<RateInfo> listFiltered = new ArrayList<>();
		int count = 0;
		for (RateInfo ri : listNeedFilter)
		{
			if (this.utilityMatrix[ri.userId][itemId] != 0 
					&& ri.sim > 0
					)
			{
				ri.rate = this.utilityMatrix[ri.userId][itemId];
				listFiltered.add(ri);
				count +=1;
				if (count >= this.K_NEAREST_NEIGHBOR) break;
				//System.out.println("user ID: " + ri.userId +  "\titemId:" + itemId + "\tsim: " + ri.sim);
			}
		}
		
		return listFiltered;
	}
	
	//sortedSim la danh sach nhung user da rate item theo thu tu sim giam dan
	private float predictRateUserToItem(ArrayList<RateInfo> sortedSim)
	{
		float ratePredict = 0;
		float sumRatexSim = 0;
		float sumSim = 0;
		for (RateInfo ri : sortedSim)
		{		
				sumRatexSim += ri.rate * ri.sim;
				sumSim += Math.abs(ri.sim);		
		}
		if (Float.isNaN(sumSim)) sumSim = 0.0000000001f;
		ratePredict = sumRatexSim / sumSim;
		return ratePredict;
	}
	
	public void predictAllUserForEvaluate()
	{
		if (!NEED_EVALUATE) return;
		long count = 0;
		for (int i = 0; i< this.numUser; i++)
		{
			ArrayList<Rate> listRate = predictItemsForUser(i, 1);
			for (Rate r: listRate)
			{
				this.predictMatrix[i][r.itemId] = r.rate;
				count += 1;
			}
			//System.out.println("fill complete predict for user " + i);
		}
		System.out.println("fill complete "+ count + " predict " );
	}
	
	public void printPredictMatrix()
	{
		for (int i=1; i< this.numUser; i++)
	      {
			if (!this.userRatingInfo.containsKey(i))
				continue;
	    	  for (int j=1; j< this.numItem; j++)
	    		 // if (normalizedMatrix[i][j] != 0) 
	    			  System.out.print(predictMatrix[i][j] + "\t");
	    	  System.out.println();
	      }
	}
	
	public double RMSE()
	{
		double rmse = 0;
		int count = 0;
		predictAllUserForEvaluate();
		
		for (int i = 1; i < this.numUser ; i++)
			for (int j = 0; j < this.numItem ; j++)
			{
				if (this.testMatrix[i][j] != 0)
				{
					
					double delta = Math.pow(this.testMatrix[i][j] - this.predictMatrix[i][j], 2);
					//System.out.println("delta =  " + delta + "\t" + this.testMatrix[i][j] +"\t"+ this.predictMatrix[i][j] );
					rmse += delta;			
					count += 1;
					if (Double.isNaN(rmse))
					{
						System.out.println("REAL : " + this.testMatrix[i][j] + "\tPREDICT: " + this.predictMatrix[i][j] + "\t"+i+"\t"+j);
					    break;
					}
				}
				if (Double.isNaN(rmse)) break;
			}
		System.out.println("Count = " + count);
		rmse = Math.sqrt(rmse / count);
		return rmse;
	}
}
