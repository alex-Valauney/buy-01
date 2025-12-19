import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { ApiService } from './api.service';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private tokenKey = 'auth_token';
    private currentUserSubject = new BehaviorSubject<any>(null);
    public currentUser$ = this.currentUserSubject.asObservable();

    constructor(
        private api: ApiService,
        private router: Router
    ) {
        this.loadUser();
    }

    private loadUser() {
        const token = this.getToken();
        if (token) {
            // Decode simple du payload JWT pour récupérer le user/role sans appel réseau bloquant
            // En prod, on validerait via /api/users/profile
            const payload = this.parseJwt(token);
            if (payload) {
                this.currentUserSubject.next({
                    email: payload.sub,
                    role: payload.role,
                    id: payload.id
                });
            }
        }
    }

    register(data: any): Observable<any> {
        return this.api.post('/auth/register', data);
    }

    login(credentials: any): Observable<any> {
        return this.api.post<any>('/auth/login', credentials).pipe(
            tap(response => {
                this.setToken(response.token);
                this.loadUser();
            })
        );
    }

    logout() {
        localStorage.removeItem(this.tokenKey);
        this.currentUserSubject.next(null);
        this.router.navigate(['/login']);
    }

    getToken(): string | null {
        return localStorage.getItem(this.tokenKey);
    }

    private setToken(token: string) {
        localStorage.setItem(this.tokenKey, token);
    }

    private parseJwt(token: string) {
        try {
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            }).join(''));
            return JSON.parse(jsonPayload);
        } catch (e) {
            return null;
        }
    }

    isAuthenticated(): boolean {
        return !!this.getToken();
    }

    getCurrentUserValue(): any {
        return this.currentUserSubject.value;
    }
}
