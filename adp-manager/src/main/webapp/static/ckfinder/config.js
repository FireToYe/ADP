/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckfinder.com/license
*/

CKFinder.customConfig = function( config )
{
	// Define changes to default configuration here.
	// For the list of available options, check:
	// http://docs.cksource.com/ckfinder_2.x_api/symbols/CKFinder.config.html

	// Sample configuration options:
	config.uiColor = '#f7f5f4';
	var curLang=cookie("lang");
	if(curLang=="zh_TW"){
		config.language = 'zh-tw';
	}else if(curLang=="zh_CN"){
		config.language = 'zh-cn';
	}else{
		config.language = 'en';
	}

	config.removePlugins = 'basket,help';
	config.defaultSortBy = 'date';
	
};
