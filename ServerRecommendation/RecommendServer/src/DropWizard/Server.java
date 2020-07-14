package DropWizard;

import java.io.IOException;

import Recommend.RecommendationSpark;
import Recommend.ShareLoopGroup;
import io.dropwizard.setup.Environment;
import Database.DatabaseUtils;
import Database.NeighborhoodBased;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Server extends io.dropwizard.Application<RecommendServerConfiguration> {

	@Override
	public void run(RecommendServerConfiguration configuration, Environment environment) throws Exception {
		// TODO Auto-generated method stub
		environment.jersey().register(new RecommendController(environment.getValidator()));
	}

	public static void main(String[] args) throws Exception {
		DatabaseUtils.initConnection();
		//DatabaseUtils.loadRatings();
		//ArrayList<Integer> listId = new ArrayList<>();
		//listId.add(1);
		//DatabaseUtils.getListSanPhamByListID(listId);
		
		//initRecommendationSystem();

		new Server().run("server", "application.yml");
	}
	
	private static void initRecommendationSystem() throws IOException
	{
		RecommendationSpark.init();
	}
	
}
