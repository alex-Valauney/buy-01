export interface Product {
    id?: string;
    name: string;
    description: string;
    price: number;
    stockQuantity?: number;
    sellerId: string;
    imageUrls?: string[];
}
