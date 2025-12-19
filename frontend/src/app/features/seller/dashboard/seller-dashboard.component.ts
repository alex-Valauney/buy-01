import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../core/services/product.service';
import { Product } from '../../../core/models/product.model';
import { AuthService } from '../../../core/services/auth.service';

@Component({
    selector: 'app-seller-dashboard',
    templateUrl: './seller-dashboard.component.html'
})
export class SellerDashboardComponent implements OnInit {
    products: Product[] = [];
    userId: string = '';

    constructor(
        private productService: ProductService,
        private auth: AuthService
    ) { }

    ngOnInit(): void {
        const user = this.auth.getCurrentUserValue();
        if (user) {
            this.userId = user.id;
            this.loadMyProducts();
        }
    }

    loadMyProducts() {
        this.productService.getMyProducts(this.userId).subscribe(products => {
            this.products = products;
        });
    }

    deleteProduct(id: string) {
        if (confirm('Are you sure?')) {
            this.productService.deleteProduct(id).subscribe(() => {
                this.loadMyProducts();
            });
        }
    }
}
