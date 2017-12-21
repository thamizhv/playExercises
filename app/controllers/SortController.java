package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import models.Sort;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class SortController extends Controller {

	private final Form<Sort> form;
	private final List<Integer> Sorted_Result = new ArrayList<>();
	private final Set<Integer> Set_Result = new TreeSet<>();
	
	@Inject
	public SortController(FormFactory formFactory){
		this.form = formFactory.form(Sort.class);	
	}
	
	public Result sort(){
		return ok(sort_page.render(form,Sorted_Result));
	}
	
	public Result save(){
		final Form<Sort> sortForm =form.bindFromRequest();
		Sort sortObj = sortForm.get();
		Set_Result.clear();
		Set_Result.add(sortObj.number1);
		Set_Result.add(sortObj.number2);
		Set_Result.add(sortObj.number3);
		Set_Result.add(sortObj.number4);
		Set_Result.add(sortObj.number5);
		Sorted_Result.clear();
		Sorted_Result.addAll(Set_Result);
		return redirect("/sort");
	}
}
