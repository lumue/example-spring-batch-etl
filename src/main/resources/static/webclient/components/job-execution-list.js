Polymer('job-execution-list', {
	ready : function() {
		this.data = [ {
			what : 'Hello',
			who : 'World'
		}, {
			what : 'Goodbye',
			who : 'DOM APIs'
		}, {
			what : 'Hello',
			who : 'Declarative'
		}, {
			what : 'Goodbye',
			who : 'Imperative'
		} ];
	}
	
});