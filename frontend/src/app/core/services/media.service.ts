import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class MediaService {
    private baseUrl = '/api/media';

    constructor(private http: HttpClient) { }

    uploadFile(file: File): Observable<any> {
        const formData: FormData = new FormData();
        formData.append('file', file);

        return this.http.post<any>(`${this.baseUrl}/upload`, formData);
    }
}
