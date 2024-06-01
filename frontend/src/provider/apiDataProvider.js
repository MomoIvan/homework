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
    upload: (resource, file) => {
        const url = `${apiUrl}/${resource}`;
        const formData = new FormData();
        formData.append('file', file);

        return fetch(url, {
            method: 'POST',
            body: formData,
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            return response.json();
        })
        .then(json => ({ data: json }))
        .catch(error => {
            throw new Error(`Error uploading file: ${error.message}`);
        });
    }
};

export default ApiDataProvider;