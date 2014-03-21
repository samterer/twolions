function ApplicationWindow() {
	
	//load dependencies
	var listView = require('ui/ListView');

	//create object instance
	var self = Ti.UI.createWindow({
		backgroundColor:'white',
		exitOnClose:true
	});
	
	//construct UI
	var listdata = new ListView();
	listdata.top = 0;
	self.add(listdata);
	
	//return instance from constructor
	return self;

}

module.exports = ApplicationWindow;