angular.module('MediaApp', [])
  .controller('MediaListController', function($http) {
    var mediaList = this;
    mediaList.conversion = {
    	mime: 'text/plain',
    	options: [
    		{mime: 'text/plain', ext: 'txt', descr: 'Text'},
    		{mime: 'application/pdf', ext: 'pdf', descr: 'Pdf'},
    		{mime: 'application/pdf;pdfA=true', ext: 'pdf', descr: 'Pdf/A'},
    		{mime: 'application/json;meta=true', ext: 'json', descr: 'Meta'},
    		{mime: 'image/jpg', ext: 'jpg', descr: 'JPeg'},
    		{mime: 'image/jpg;gray=true', ext: 'jpg', descr: 'JPeg/Gray'},
    		{mime: 'image/jpg;thumb=150x150', ext: 'jpg', descr: 'JPeg/Thumb'},
    		{mime: 'image/png', ext: 'png', descr: 'Png'},
    		{mime: 'image/png;gray=true', ext: 'png', descr: 'Png/Gray'},
    		{mime: 'image/png;thumb=150x150', ext: 'png', descr: 'Png/Thumb'},
    		{mime: 'image/tif', ext: 'tif', descr: 'Tiff'},
    		{mime: 'image/tif;gray=true', ext: 'tif', descr: 'Tiff/Gray'},

    		{mime: 'this/that', ext: 'wrng', descr: 'Wrong'}
    	]
    };
    mediaList.media = [];

    mediaList.getMediaList = function() {
        $http.get('/media/').
            success(function(data, status, headers, config) {
                if (data._embedded != undefined) {
                    mediaList.media = [];
                    angular.forEach(data._embedded.media, function(m) {
                        mediaList.media.push(m);
                    });
                }
            });
        };
    mediaList.getMediaList();

    mediaList.getHref = function(m) {
        return m._links["self"].href
    };
    
    mediaList.getHrefTxt = function(m) {
        return m._links["self"].href+'/txt'
    };
    
    mediaList.getDownTxt = function(m) {
        return "mediaList.download('" + m._links["self"].href +"')";
    };
    
    mediaList.getExtension = function() {
    	var retVal = '' ;
    	mediaList.conversion.options.forEach(function(element, index, array) {
    		if ( element.mime.toString()
    				== mediaList.conversion.mime ) {
    			retVal = element.ext;
    		}
    	}
    	);
    	return retVal;
    };

    mediaList.getDescription = function() {
    	var retVal = 'Unknown' ;
    	mediaList.conversion.options.forEach(function(element, index, array) {
    		if ( element.mime.toString()
    				== mediaList.conversion.mime ) {
    			retVal = element.descr;
    		}
    	}
    	);
    	return retVal;
    };

    mediaList.upload = function() {
        var f = document.getElementById('mediumElement').files[0];
        var medium = {name: f.name, summary: mediaList.summary};

        $http.post('/media/', medium).
            then(function(response) {
                var fd = new FormData();
                fd.append('file', f); // adding 'file' element to form !!!! doing copy form original
                return $http.put(response.headers("Location"), fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                });
            })
            .then(function(response) {
                mediaList.title = "";
                mediaList.keywords = "";
                mediaList.getMediaList();
                document.getElementById('mediumElement').files[0] = undefined;
            });
    };
    
    mediaList.downloadFile = function (u) {
        var ext = this.getExtension();
        $http({
            method: 'GET',
            url: u._links["self"].href,
            //params: { name: name },
            responseType: 'arraybuffer',
        	headers: {
                'accept': this.conversion.mime
            }
        }).success(function (data, status, headers) {
            headers = headers();
     
            var filename = headers['x-file-name'] || ("file."+ext);
            if ( !filename.endsWith("."+ext) ) {
            	filename += "."+ext
            }
            var contentType = headers['content-type'];
     
            var linkElement = document.createElement('a');
            try {
                var blob = new Blob([data], { type: contentType });
                var url = window.URL.createObjectURL(blob);
     
                linkElement.setAttribute('href', url);
                linkElement.setAttribute("download", filename);
     
                var clickEvent = new MouseEvent("click", {
                    "view": window,
                    "bubbles": true,
                    "cancelable": false
                });
                linkElement.dispatchEvent(clickEvent);
                window.URL.revokeObjectURL();
            } catch (ex) {
                console.log(ex);
            }
        }).error(function (data) {
            console.log(data);
        });
    };
    
  });