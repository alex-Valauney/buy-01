import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { ProductListComponent } from './features/product/product-list/product-list.component';
import { SellerDashboardComponent } from './features/seller/dashboard/seller-dashboard.component';
import { ProductFormComponent } from './features/seller/product-form/product-form.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'products', component: ProductListComponent },
  { path: 'seller/dashboard', component: SellerDashboardComponent },
  { path: 'seller/product/new', component: ProductFormComponent },
  { path: 'seller/product/edit/:id', component: ProductFormComponent },
  { path: '', redirectTo: '/products', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
