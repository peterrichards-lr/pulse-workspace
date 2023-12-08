const buildGraphQlQuery = (operation, requestBody, parameters) => {
    var parameterList = '';
    if (parameters) {
        const objectEntries = Object.entries(parameters)
        for (const [key, value] of objectEntries) {
            parameterList += parameterList.length > 0 ? `,${key}:${value}` : `${key}:${value}`;
        }
    }

    return JSON.stringify({
        query:
            `{ ${operation}(${parameterList}) { ${requestBody} } }`
    });
}

const parseGraphQlQueryResponse = (operation, response) => {
    const {data} = JSON.parse(response);
    return data[operation] ? data[operation] : {};
}

export {
    buildGraphQlQuery,
    parseGraphQlQueryResponse,
};