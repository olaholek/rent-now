import {Component, OnInit} from '@angular/core';
import {FileUpload} from "primeng/fileupload";
import {MessageService} from "primeng/api";

@Component({
    selector: 'app-step2-photos',
    templateUrl: './step2-photos.component.html',
    styleUrls: ['./step2-photos.component.scss']
})
export class Step2PhotosComponent implements OnInit {

    uploadedFiles: any[] = [];

    constructor(private messageService: MessageService) {
    }

    ngOnInit(): void {
    }

    onUpload(event: FileUpload) {
        for (let file of event.files) {
            this.uploadedFiles.push(file);
        }

        this.messageService.add({severity: 'info', summary: 'File Uploaded', detail: ''});
    }
}
