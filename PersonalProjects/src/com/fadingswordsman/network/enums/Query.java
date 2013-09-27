package com.fadingswordsman.network.enums;

public enum Query
{
	GET,
	POST(new QueryConstructor(){
		public String construct(String query)
		{
			StringBuilder sb = new StringBuilder(" ");
			sb.append(query);
			return sb.toString();
		}
	});
	
	private QueryConstructor queryConstructor;
	
	public String getQuery(String query)
	{
		StringBuilder returnQuery = new StringBuilder();
		returnQuery.append(this.name());
		returnQuery.append(queryConstructor.construct(query));
		return returnQuery.toString();
	}
	
	private Query()
	{
		
	}
	
	private Query(QueryConstructor queryConstructor)
	{
		this.queryConstructor = queryConstructor;
	}
	
	private interface QueryConstructor
	{
		public String construct(String input);
	}
}
