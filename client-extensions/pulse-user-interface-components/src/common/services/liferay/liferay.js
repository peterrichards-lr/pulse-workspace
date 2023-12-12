export const Liferay = window.Liferay || {
    OAuth2: {
        getAuthorizeURL: () => '',
        getBuiltInRedirectURL: () => '',
        getIntrospectURL: () => '',
        getTokenURL: () => '',
        getUserAgentApplication: (serviceName) => {
        },
    },
    OAuth2Client: {
        FromParameters: (options) => {
            return {};
        },
        FromUserAgentApplication: (userAgentApplicationId) => {
            return {
                _getOrRequestToken: async () => ({
                    access_token: ''
                }),
                homePageURL: ''
            };
        },
        fetch: (url, options = {}) => {
        },
    },
    Icons: {
        spritemap:
            "http://localhost:8080/o/classic-theme/images/clay/icons.svg",
        controlPanelSpritemap:
            "http://localhost:8080/o/admin-theme/images/clay/icons.svg"
    },
    Language: {
        available: {
            "en_US": "English (United States)"
        },
        direction: {
            "en_US": "ltr"
        },
        get: () => ''
    },
    ThemeDisplay: {
        getCompanyGroupId: () => 0,
        getScopeGroupId: () => 0,
        getSiteGroupId: () => 0,
        isSignedIn: () => {
            return false;
        },
    },
    authToken: '',
    Util: {
        openToast: () => {
        }
    }
}
