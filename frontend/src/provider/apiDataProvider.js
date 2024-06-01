import { fetchUtils } from 'react-admin';
import { stringify } from 'query-string';

const apiUrl = 'http://localhost:8080/api/v1';
const httpClient = fetchUtils.fetchJson;

const ApiDataProvider = {
    getList: (resource, params) => {
        const query = { ...params.filter };
        const url = `${apiUrl}/${resource}?${stringify(query)}`

        return httpClient(url).then(({ headers, json }) => ({
            data: json,
        }));
    },
};

export default ApiDataProvider;