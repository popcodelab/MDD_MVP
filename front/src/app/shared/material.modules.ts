import {NgModule} from '@angular/core';
import {MatButtonModule} from "@angular/material/button";
import {MatCardModule} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatToolbar} from "@angular/material/toolbar";


@NgModule({
  imports: [
    MatCardModule,
    MatButtonModule,
    MatIcon,
    //MatFormField,
    MatInput,
    MatFormFieldModule,
    MatSidenavModule,
    MatToolbar
  ],
  exports: [
    MatCardModule,
    MatButtonModule,
    MatIcon,
    //MatFormField,
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

