package controllers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import models.Cities;
import models.Sort;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import webservicex_city.GetCitiesByCountry;
import webservicex_city.GlobalWeather;
import webservicex_city.GlobalWeatherSoap;
import webservicex_city.ObjectFactory;
import webservicex_country.Country;
import webservicex_country.CountrySoap;

public class CityController extends Controller {

	private Form<Cities> form;
	private List<String> cityList = new ArrayList<String>();
	private List<String> countryList = new ArrayList<String>();

	@Inject
	public CityController(FormFactory formFactory){
		this.form = formFactory.form(Cities.class);	
	}
	public Result show(){

		Country country = new Country();
		CountrySoap countrySoap = country.getCountrySoap();
		String countries = countrySoap.getCountries();
		try{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource src = new InputSource();
			src.setCharacterStream(new StringReader(countries));
			Document doc = builder.parse(src);

			NodeList list = doc.getElementsByTagName("Table");

			for(int i = 0;i<list.getLength();i++){

				countryList.add(doc.getElementsByTagName("Name").item(i).getTextContent());

			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return ok(city_page.render(form,countryList,cityList));
	}

	public Result postshow(){

		final Form<Cities> cityForm =form.bindFromRequest();
		Cities cityObj = cityForm.get();
		GlobalWeather globalWeather = new GlobalWeather();
		GlobalWeatherSoap globalWeatherSoap = globalWeather.getGlobalWeatherSoap();
		String cities = globalWeatherSoap.getCitiesByCountry(cityObj.getCountry());

		cityList.clear();

		try{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource src = new InputSource();
			src.setCharacterStream(new StringReader(cities));
			Document doc = builder.parse(src);

			NodeList list = doc.getElementsByTagName("City");

			for(int i = 0;i<list.getLength();i++){

				if(doc.getElementsByTagName("Country").item(i).getTextContent().equals(cityObj.getCountry())){		
					cityList.add(doc.getElementsByTagName("City").item(i).getTextContent());	
				}
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return redirect("/cities");
	}

}
