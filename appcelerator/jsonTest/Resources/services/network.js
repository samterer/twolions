var url = "http://mysafeinfo.com/api/data?list=englishmonarchs&format=json";

var Monarch = require('model/Monarch');

//implement service interface
exports.getList = function(callback) {

	var xhr = Ti.Network.createHTTPClient({
		onload : function() {
			
			Ti.API.info("prepare to onload list");
			
			var data = JSON.parse(this.responseText);
			var monarchs = [];

			for (var i = 0, l = data.length; i < l; i++) {
				var monarch = new Monarch(data[i].nm, data[i].cty, data[i].yrs);

				Ti.API.info('item ' + i + " charged.");

				monarchs.push(monarch);
			}

			//call callback function with an array of Todos
			callback.call(this, monarchs);

		},
		onerror : function(e) {
			Ti.API.info("STATUS: " + this.status);
			Ti.API.info("TEXT:   " + this.responseText);
			Ti.API.info("ERROR:  " + e.error);
			alert('There was an error retrieving the remote data. Try again.');
		},
		timeout : 5000
	});

	xhr.open('GET', url);
	xhr.send();
};
