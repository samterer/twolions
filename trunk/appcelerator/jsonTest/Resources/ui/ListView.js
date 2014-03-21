function ListView() {

	Ti.API.info("load ListView");

	var url = "http://mysafeinfo.com/api/data?list=englishmonarchs&format=json";

	//create object instance
	var self = Ti.UI.createTableView({
		data : [{
			title : 'Loading Data...',

		}],
	});

	//load dependencies
	var network = require('services/network');
	var Monarch = require('model/Monarch');

	//create object instance
	var self = Ti.UI.createView({
		backgroundColor : '#cdcdcd',
	});

	// state variable
	var monarchs = [];

	function loadData() {
		var tableData = [];

		// charge list
		monarchs = network.getList();

		Ti.API.info("load list...");

		//loop each item in the json object
		for (var i = 0; i < monarchs.length; i++) {

			var monarch = monarchs[i];

			//create a table row
			var row = Titanium.UI.createTableViewRow({
				hasChild : true,
				className : 'recipe-row',
				backgroundColor : '#fff',
				filter : monarch.nm, //this is the data we want to search on (title)
				title : monarch.nm,
				content : monarch.yrs,
				link : "https://www.google.com/search?q=" + monarch.nm
			});

			//title label
			var titleLabel = Titanium.UI.createLabel({
				text : monarch.nm,
				font : {
					fontSize : 14,
					fontWeight : 'bold'
				},
				left : 70,
				top : 5,
				height : 20,
				width : 210,
				color : '#000'
			});
			row.add(titleLabel);

			//content label
			var contentLabel = Titanium.UI.createLabel({
				text : monarch.yrs,
				font : {
					fontSize : 10,
					fontWeight : 'normal'
				},
				left : 70,
				top : 25,
				height : 40,
				width : 200,
				color : '#000'
			});
			if (contentLabel.text == '') {
				contentLabel.text = 'No year is available.';
			}
			row.add(contentLabel);

			//add our little icon to the left of the row
			/*			var iconImage = Titanium.UI.createImageView({
			image : 'images/foodicon.jpg',
			width : 50,
			height : 50,
			left : 10,
			top : 10
			});
			row.add(iconImage);
			*/
			//add the table row to our tableData[] object
			tableData.push(row);

		}
		table.setData(tableData);

	}


	self.add(table);

	//loadData();
	return self;

}

module.exports = ListView;

