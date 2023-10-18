import {Injectable} from '@angular/core';
import {MessageService} from "primeng/api";

@Injectable({
    providedIn: 'root'
})
export class ToastService {

    constructor(private messageService: MessageService) {
    }

    showSuccess(message: string, duration?: number, sticky?: boolean) {
        if (!duration) {
            duration = 3 * 1000;
        }
        if (!sticky) {
            sticky = false;
        }
        this.messageService.add({
            life: duration,
            severity: 'success',
            summary: 'Success',
            detail: message,
            sticky: sticky
        });
    }

    showInfo(message: string, duration?: number, sticky?: boolean) {
        if (!duration) {
            duration = 3 * 1000;
        }
        if (!sticky) {
            sticky = false;
        }
        this.messageService.add({
            life: duration,
            severity: 'info',
            summary: 'Info',
            detail: message,
            sticky: sticky
        });
    }

    showWarn(message: string, duration?: number, sticky?: boolean) {
        if (!duration) {
            duration = 3 * 1000;
        }
        if (!sticky) {
            sticky = false;
        }
        this.messageService.add({
            life: duration,
            severity: 'warn',
            summary: 'Warn',
            detail: message,
            sticky: sticky
        });
    }

    showError(message: string, duration?: number, sticky?: boolean) {
        if (!duration) {
            duration = 3 * 1000;
        }
        if (!sticky) {
            sticky = false;
        }
        this.messageService.add({
            life: duration,
            severity: 'error',
            summary: 'Error',
            detail: message,
            sticky: sticky
        });
    }
}
