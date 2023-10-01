package main.auxiliary.filter;

public class Condition {
	public String field;
	public Object value;

	public Condition(Object value, String field) {
		this.value = value;
		this.field = field;
	}

	public static class Builder {
		private Object value;
		private String field;

		public Builder setValue(Object value) {
			this.value = value;
			return this;
		}

		public Builder setField(String field) {
			this.field = field;
			return this;
		}

		public Condition build() {
			return new Condition(value, field);
		}
	}
}
