import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../../core/services/product.service';
import { MediaService } from '../../../core/services/media.service';
import { Product } from '../../../core/models/product.model';

@Component({
    selector: 'app-product-form',
    templateUrl: './product-form.component.html'
})
export class ProductFormComponent implements OnInit {
    productForm: FormGroup;
    isEditMode = false;
    productId: string | null = null;
    uploadedImageUrls: string[] = [];

    constructor(
        private fb: FormBuilder,
        private productService: ProductService,
        private mediaService: MediaService,
        private route: ActivatedRoute,
        private router: Router
    ) {
        this.productForm = this.fb.group({
            name: ['', Validators.required],
            description: ['', Validators.required],
            price: [0, [Validators.required, Validators.min(0)]],
            stockQuantity: [0, [Validators.required, Validators.min(0)]]
        });
    }

    ngOnInit(): void {
        this.productId = this.route.snapshot.paramMap.get('id');
        if (this.productId) {
            this.isEditMode = true;
            this.productService.getProducts().subscribe(products => {
                // In a real app we would have getProductById endpoint, but here we can reuse list or add getById
                // Let's assume we filter from the list or add getById to service.
                // For now, finding in list if available (or fetch all and find).
                const product = products.find(p => p.id === this.productId);
                if (product) {
                    this.patchForm(product);
                }
            });
        }
    }

    patchForm(product: Product) {
        this.productForm.patchValue({
            name: product.name,
            description: product.description,
            price: product.price,
            stockQuantity: product.stockQuantity
        });
        this.uploadedImageUrls = product.imageUrls || [];
    }

    onFileSelected(event: any) {
        const file: File = event.target.files[0];
        if (file) {
            this.mediaService.uploadFile(file).subscribe({
                next: (response) => {
                    this.uploadedImageUrls.push(response.fileDownloadUri);
                },
                error: (err) => alert('Upload failed')
            });
        }
    }

    onSubmit() {
        if (this.productForm.invalid) return;

        const productData: Product = {
            ...this.productForm.value,
            imageUrls: this.uploadedImageUrls,
            sellerId: '' // Backend handles sellerId from token
        };

        if (this.isEditMode && this.productId) {
            this.productService.updateProduct(this.productId, productData).subscribe({
                next: () => this.router.navigate(['/seller/dashboard']),
                error: (err) => alert('Update failed')
            });
        } else {
            this.productService.createProduct(productData).subscribe({
                next: () => this.router.navigate(['/seller/dashboard']),
                error: (err) => alert('Creation failed')
            });
        }
    }
}
