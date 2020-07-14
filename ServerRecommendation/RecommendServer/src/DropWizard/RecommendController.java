package DropWizard;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Database.DatabaseUtils;
import Recommend.RecommendationSpark;
import Recommend.SANPHAM;
import scala.collection.mutable.WrappedArray;

@Path("/recommend")
@Produces(MediaType.APPLICATION_JSON)
public class RecommendController {
	private final Validator validator;

	public RecommendController(Validator validator) {
		this.validator = validator;
	}

	@GET
	public Response getRatingsPrediction() {
		//RecommendationSpark.recommendForAllUser(50);
		return Response.ok("response GET").build();
	}

	@GET
	@Path("/{id}")
	public Response getRatingsPredictionById(@PathParam("id") Integer id) throws SQLException {

		return Response.ok(DatabaseUtils.predictForUserNumItem(id,  10)).build();
		
	}

	@GET
	@Path("/{id}/{numItem}")
	public Response getRatingsPredictionById(@PathParam("id") Integer id, @PathParam("numItem") Integer numItem)
			throws SQLException {
	
		return Response.ok(DatabaseUtils.predictForUserNumItem(id,  numItem)).build();
		/*
		if (id == 0) {
			ArrayList<SANPHAM> tmp = DatabaseUtils.getListSanPhamGoiYAnonymous();
			return Response.ok(tmp).build();
			// return Response.ok(DatabaseUtils.getListSanPhamGoiYAnonymous()).build();
		} else {

			return Response.ok(RecommendationSpark.recommendForUserById(id, numItem)).build();
			// return Response.ok(DatabaseUtils.getListSanPhamGoiYAnonymous()).build();
		}*/

	}
}
