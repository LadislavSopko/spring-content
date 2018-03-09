angular.module('MediaApp', [])
  .controller('MediaListController', function($http) {
    var mediaList = this;
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
    
    mediaList.downloadFile = function (u, mime, ext) {
        $http({
            method: 'GET',
            url: u._links["self"].href,
            //params: { name: name },
            responseType: 'arraybuffer',
        	headers: {
                'accept': mime
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