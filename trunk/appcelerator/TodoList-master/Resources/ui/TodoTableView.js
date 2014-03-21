function TodoTableView() {
	//load dependencies
	var datastore = require('services/datastore');

	//create object instance
	var self = Ti.UI.createTableView({
		data : [{
			title : 'Loading Data...',

		}],
	});

	//state variable
	var todos = [];

	var pb = Titanium.UI.createProgressBar({
		top : 10,
		width : 250,
		height : 'auto',
		min : 0,
		max : 10,
		value : 0,
		color : '#fff',
		message : 'Loading....',
		font : {
			fontSize : 14,
			fontWeight : 'bold'
		},
		style : Titanium.UI.iPhone.ProgressBarStyle.PLAIN,
	});
	self.add(pb);
	pb.show();

	//add behavior
	function loadData() {
		var tableData = [];
		todos = datastore.getList();

		for (var i = 0, l = todos.length; i < l; i++) {
			var todo = todos[i];

			//alert("todo: " + i);

			tableData.push({
				title : todo.text,
				hasCheck : todo.done,
				todoObject : todo
			});
		}
		
		self.remove(pb);
		//pb.remove();
		
		self.setData(tableData);
	}

	//toggle done state of todo item on click
	self.addEventListener('click', function(e) {
		var todo = todos[e.index];
		todo.done = !todo.done;
		datastore.saveTodo(todo);
		//update row UI
		e.row.hasCheck = todo.done;
	});

	//reload data when we're told that it has changed
	self.addEventListener('todosUpdated', loadData);

	//initialize and return instance from constructor
	loadData();
	return self;
}

module.exports = TodoTableView;
