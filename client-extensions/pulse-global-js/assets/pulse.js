const pulseHelper = {
    createObjectEntry: (objectUrl, objectData) => {
        return Liferay.Util.fetch(objectUrl, {
            method: 'POST', headers: {
                "Content-Type": "application/json"
            }, body: JSON.stringify(objectData)
        })
            .then((response) => {
                const {status} = response;
                const responseContentType = response.headers.get('content-type');
                if (status === 204) {
                    return {status};
                } else if (response.ok && responseContentType === 'application/json') {
                    return response.json();
                } else {
                    return response.text();
                }
            });
    },

    syncFetch: (url) => {
        if (!url) {
            return;
        }
        const request = new XMLHttpRequest();
        request.open("GET", url, false); // `false` makes the request synchronous
        request.send(null);
        return {
            status: request.status,
            responseText: request.responseText
        };
    },
    getJsonDate: () => {
        return new Date().toJSON();
    },

    isJsonString: (str) => {
        try {
            JSON.parse(str);
        } catch (e) {
            return false;
        }
        return true;
    },

    getQueryStringObj: () => {
        const search = location.search.substring(1);
        if (!search) { // If it is not set then there is no query string to map
            console.debug("There is no query string, so nothing to do");
            return;
        }
        try {
            return JSON.parse('{"' + decodeURI(search).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g, '":"') + '"}');
        } catch (e) {
            console.error("The query string is invalid");

        }
    },

    getCookie: (cname) => {
        let name = cname + "=";
        let decodedCookie = decodeURIComponent(document.cookie);
        let ca = decodedCookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    },

    setCookie: (cname, cvalue, exdays) => {
        let expires = undefined;
        if (exdays) {
            const d = new Date();
            d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
            expires = "expires=" + d.toUTCString();
        }
        if (expires) document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/"; else document.cookie = cname + "=" + cvalue + ";path=/";
    },

    deleteCookie: (cname) => {
        if(getCookie(name)) {
            document.cookie = name + "=/;path=/;expires=Thu, 01 Jan 1970 00:00:01 GMT";
        }
    },
    
    buildAnalyticsEventData: () => {
        const userIdStr = themeDisplay.getUserId();
        const userId = isNaN(userIdStr) ? userIdStr : parseInt(userIdStr);
        return {
            userId: userId, actionAt: getJsonDate(),
        };
    }
}