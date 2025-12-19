import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';

@Injectable({
    providedIn: 'root'
})
export class ProductService {
    private endpoint = '/products';
    constructor(private api: ApiService) { }

    getProducts(): Observable<Product[]> {
        return this.api.get<Product[]>(this.endpoint);
    }

    createProduct(product: Product): Observable<Product> {
        return this.api.post(this.endpoint, product);
    }

    updateProduct(id: string, product: Product): Observable<Product> {
        return this.api.put(`${this.endpoint}/${id}`, product);
    }

    deleteProduct(id: string): Observable<void> {
        return this.api.delete(`${this.endpoint}/${id}`);
    }

    getMyProducts(sellerId: string): Observable<Product[]> {
        return this.api.get<Product[]>(`${this.endpoint}/my-products`);
    }
}
