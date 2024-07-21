import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './pages/home/home.component';
import {RegisterComponent} from "./features/auth/pages/register/register.component";
import {PageNotFoundComponent} from "./core/components/page-not-found/page-not-found.component";
import {ContentLayoutComponent} from "./layout/content-layout/content-layout.component";
import {MeComponent} from "./features/auth/pages/me/me.component";
import {LoginComponent} from "./features/auth/pages/login/login.component";

// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
const routes: Routes = [
  {path: '', component: HomeComponent},
  {
    path: '',
    component: ContentLayoutComponent,
    children: [
      {path: 'register', component: RegisterComponent},
      {path: 'login', component: LoginComponent},
      {path: 'me', component: MeComponent },
      {path: '**', component: PageNotFoundComponent}
    ]
  },
  {
    path: '**', // wildcard
    component: PageNotFoundComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
