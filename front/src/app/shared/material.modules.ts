import {NgModule} from '@angular/core';
import {MatButtonModule} from "@angular/material/button";
import {MatCardModule} from "@angular/material/card";
import {MatIcon, MatIconModule, MatIconRegistry} from "@angular/material/icon";
import {MatFormField, MatFormFieldModule} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatToolbar} from "@angular/material/toolbar";


@NgModule({
  imports: [
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatIcon,
    MatFormField,
    MatInput,
    MatFormFieldModule,
    MatSidenavModule,
    MatToolbar
  ],
  exports: [
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatIcon,
    MatFormField,
    MatInput,
    MatFormFieldModule,
    MatSidenavModule,
    MatToolbar
  ]
})
/**
 * @author Pignon Pierre-Olivier
 *
 * @version 1.0
 */
export class MaterialModule {
}

