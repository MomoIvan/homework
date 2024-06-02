import React, {useState} from "react";
import {FileInput, FileField, SimpleForm, useNotify, ListContextProvider, Title, useList} from "react-admin";
import ApiDataProvider from "../provider/apiDataProvider";

const UploadSVCToolbar = () => {
    const [file, setSelectedFile] = useState(null);
    const notify = useNotify();

    const handleSave = async() => {
        try {
            const response = await ApiDataProvider.upload('orders/import', file)
            
            if (response.data.errorCode === 0) {
                notify('Upload Data Successfully!!!!', { type: 'info'});
            } else {
                notify(`Upload Data Failed!!!! ${response.data.errorMessage}`, { type: 'info'});
            }
        } catch (error) {
            notify(`Error uploading file: ${error.message}`, { type: 'warning' });
        }
    };

    return (
        <SimpleForm onSubmit={handleSave}>
            <FileInput source="" accept="text/csv" name="upload_file_input" onChange={setSelectedFile}>
                <FileField source="src" title="title"/>
            </FileInput>
        </SimpleForm>
    );
};

export const PostList = () => (
    <ListContextProvider value={useList({data: []})}>
        <div>
            <Title title="Order List" />
            <UploadSVCToolbar />
        </div>
    </ListContextProvider>
);