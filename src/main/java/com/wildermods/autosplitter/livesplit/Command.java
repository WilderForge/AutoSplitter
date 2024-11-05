package com.wildermods.autosplitter.livesplit;

import java.util.ArrayList;
import java.util.StringJoiner;

public class Command {
	
	public final String name;
	public ArrayList<Argument> args = new ArrayList<>();
	
	public Command(String name) {
		this.name = name;
	}
	
	public String toString() {
		StringBuilder ret = new StringBuilder("{\"command\": \"");
		ret.append(name);
		ret.append('\"');
		StringJoiner joiner = new StringJoiner(", ", "", "}");
		joiner.add(ret.toString());
		for(Argument arg : args) {
			joiner.add(arg.toString());
		}
		return joiner.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Command withArg(String name, Object val) {
		args.add(new Argument(name, val));
		return this;
	}
	
	public static class Argument {
		private final String name;
		private Object val;
		
		public Argument(String name, Object val) {
			this.name = name;
			this.val = val;
		}
		
		public String toString() {
			if(val instanceof String) {
				val = "\"" + val + "\"";
			}
			return "\"" + name + "\": " + val;
		}
		
	}
	
}
