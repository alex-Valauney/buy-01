import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-layout',
    templateUrl: './layout.component.html',
    styleUrls: ['./layout.component.css']
})
export class LayoutComponent implements OnInit {
    currentUser$: Observable<any>;

    constructor(private authService: AuthService) {
        this.currentUser$ = this.authService.currentUser$;
    }

    ngOnInit(): void { }

    logout() {
        this.authService.logout();
    }
}
