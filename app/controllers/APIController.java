package controllers;

import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import models.Member;
import models.MemberDAO;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class APIController extends Controller {
	
	private List<Member> memberAPIList;
	
	@Inject
	MemberDAO memdao;
	
	public Result getAllMembers(){
		
		memberAPIList = memdao.show();
		return ok(Json.toJson(memberAPIList));
	}

	public Result getMemberById(String Id){
		
	try{
		return ok(Json.toJson(memdao.showById(Id)));
	}
	catch(NullPointerException e){
		return notFound();
	}
	}
	
	public Result deleteMember(String Id){
		
		return memdao.delete(Id)?noContent():notFound();
		
	}
	
	public Result updateMember(String Id){
		
		JsonNode reqJson = request().body().asJson();
		Member a = Json.fromJson(reqJson, Member.class);
		return memdao.update(a,Id)?noContent():notFound();
	}
	
	public Result createMember(){
		JsonNode reqJson = request().body().asJson();
		Member member = Json.fromJson(reqJson, Member.class);
		memdao.add(member);
		
		Integer id =memdao.show().stream()
				.filter(mem -> mem.getName().equals(member.getName()) && mem.getAge()==member.getAge())
				.map(mem -> mem.getId())
				.reduce((a,b)->a)
				.orElse(null);

		JsonNode id1 = Json.parse("{\"id\":"+id+"}");
		
		return created(id1);
	}
}
