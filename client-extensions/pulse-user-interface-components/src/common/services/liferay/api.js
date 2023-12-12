import {Liferay} from './liferay';

const HEADER_CONTENT_TYPE_JSON = 'application/json';

const baseFetch = (url, options = {}) => {
    return fetch(url, {
        headers: {
            'Content-Type': 'application/json',
            'x-csrf-token': Liferay.authToken,
        },
        ...options,
    }).then((response) => {
        const {status} = response;
        const responseContentType = response.headers.get('Content-Type');

        if (status === 204) {
            return '';
        } else if (
            response.ok &&
            responseContentType === HEADER_CONTENT_TYPE_JSON
        ) {
            return response.json();
        } else {
            return response.text();
        }
    });
};

export default baseFetch;
