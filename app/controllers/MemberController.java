package controllers;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import models.Member;
import models.MemberDAO;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class MemberController extends Controller {
		private final FormFactory formFactory;
	    private List<Member> memberList;
	    MemberDAO memdao = new MemberDAO();

	    @Inject
	    public MemberController(FormFactory formFactory) {
	        this.formFactory = formFactory;
	    }
	    
	public Result show(){
		memberList = memdao.show();
		return ok(member_page.render(memberList));	
	}

	public Result save(){
		Member member = formFactory.form(Member.class).bindFromRequest().get();
		memdao.add(member);
		return redirect("/members");
	}
}
