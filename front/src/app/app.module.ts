import {ErrorHandler, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import {MaterialModule} from "./shared/material.modules";
import {NgOptimizedImage} from "@angular/common";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {JwtInterceptor} from "./core/interceptors/jwt.interceptor";
import {ErrorHandlerService} from "./core/services/error-handler.service";

@NgModule({
  declarations: [AppComponent, HomeComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    NgOptimizedImage,
    HttpClientModule
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: JwtInterceptor,
    multi: true
  },
    {
      provide: ErrorHandler,
      useClass: ErrorHandlerService
    }
  ],
  bootstrap: [AppComponent],
})
/**
 * The AppModule class is a module that defines the main components, imports, and providers for the application. It is the root module of the application.
 *
 * @NgModule decorator is used to define the metadata for the module. The metadata includes the declarations, imports, providers, and bootstrap components.
 *
 * The AppModule provides the following functionality:
 * - Declares the AppComponent and HomeComponent as components to be used in the application.
 * - Imports the BrowserModule, AppRoutingModule, BrowserAnimationsModule, MaterialModule, and NgOptimizedImage modules.
 * - Does not provide any additional services or providers.
 * - Sets the AppComponent as the bootstrap component for the application.
 */
export class AppModule {}
