package DropWizard;


import java.util.ArrayList;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Recommend.RecommendationSpark;
import scala.collection.mutable.WrappedArray;


@Path("/recommend")
@Produces(MediaType.APPLICATION_JSON)
public class RecommendController {
	private final Validator validator;
	public RecommendController(Validator validator)
	{
		this.validator = validator;
	}
	
	  @GET
	    public Response getRatingsPrediction() {
		    RecommendationSpark.recommendForAllUser(50);
	        return Response.ok("response GET").build();
	    }
	  @GET
	    @Path("/{id}")
	    public Response getRatingsPredictionById(@PathParam("id") Integer id) {
	       /*ItemRecommendations itemRecommendations = RecommendationRatingData.getItemRecommendations(id);	
	        if (itemRecommendations != null )
	            return Response.ok(itemRecommendations).build();
	        else
	            return Response.status(Status.NOT_FOUND).build();*/
	
		  //WrappedArray tmp = RecommendationSpark.recommendForUserById(id, 10);
		  ArrayList<Integer> tmp = RecommendationSpark.recommendForUserById(id, 10);
		  return Response.ok(tmp).build();
	    }
	  
	  @GET
	    @Path("/{id}/{numItem}")
	    public Response getRatingsPredictionById(@PathParam("id") Integer id, @PathParam("numItem") Integer numItem) {
	       /*ItemRecommendations itemRecommendations = RecommendationRatingData.getItemRecommendations(id);	
	        if (itemRecommendations != null )
	            return Response.ok(itemRecommendations).build();
	        else
	            return Response.status(Status.NOT_FOUND).build();*/
	
		  //WrappedArray tmp = RecommendationSpark.recommendForUserById(id, 10);
		  ArrayList<Integer> tmp = RecommendationSpark.recommendForUserById(id, numItem);
		  return Response.ok(tmp).build();
	    }
}
