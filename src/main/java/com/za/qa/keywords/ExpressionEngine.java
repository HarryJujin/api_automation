package com.za.qa.keywords;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.za.qa.log.LogMan;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by zhaoguocai@zhongan.com on 2016/7/12.
 */
public class ExpressionEngine {
	private static ExpressionEngine eng = null;

	public ExpressionEngine(Object[] obj)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		for (Object funcObject : obj) {
			Class<?> cls[] = funcObject.getClass().getDeclaredClasses();
			for (Class c : cls) {
				Constructor<?> cons[] = c.getConstructors();
				for (Constructor con : cons) {
					Constructor<?> constructor = c.getDeclaredConstructor(con.getParameterTypes());
					constructor.setAccessible(true);
					AbstractFunction o = (AbstractFunction) constructor.newInstance(funcObject);
					AviatorEvaluator.addFunction(o);
				}
			}
		}
	}

	/**
	 * 执行表达式
	 */
	public Object execute(String expression) {
		if(expression.contains("(")&&expression.contains(")")){
			return AviatorEvaluator.execute(expression);
		}else{
			return expression;
		}
	}

	/**
	 *
	 * 执行表表达式使用上下文环境变量
	 */
	public Object execute(String expression, Map<String, Object> env) {
		return AviatorEvaluator.execute(expression, env);
	}

	/**
	 * 执行表达式允许缓存
	 * 
	 * @param expression
	 */
	public Object execute(String expression, Map<String, Object> env, boolean cached) {
		return AviatorEvaluator.execute(expression, env, cached);
	}

	public static ExpressionEngine getEngine() {
		Object[] o = { new KeywordDefinition() };
		try {
			if (eng == null) {
				eng = new ExpressionEngine(o);
			}
		} catch (Exception e) {
			String errorMsg = String.format("初始化ExpressionEngine對象失敗. Caused By:%s", e.getMessage());
			LogMan.getLoger().error(errorMsg, e);
			return null;
		}
		return eng;
	}
}
