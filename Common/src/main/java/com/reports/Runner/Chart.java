package com.reports.Runner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Chart {
	public static void main(String[] args) throws ScriptException, IOException, NoSuchMethodException {
		ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        System.out.println("okay1");
        FileInputStream fileInputStream = new FileInputStream("./myChart.js");
        System.out.println("okay2");
        if (fileInputStream != null){
         BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
         engine.eval(reader);
         System.out.println("okay3");
        // Invocable javascriptEngine = null;
         System.out.println("okay4");
        Invocable invocableEngine = (Invocable)engine;
         System.out.println("okay5");
         int x=0;
         System.out.println("invocableEngine is : "+invocableEngine);
         Object object = invocableEngine.invokeFunction("backend_message",x);

         System.out.println(object.toString());
        }
	}
	
}
