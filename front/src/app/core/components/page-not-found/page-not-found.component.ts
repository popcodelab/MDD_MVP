import { Component } from '@angular/core';
import {MatButton} from "@angular/material/button";
import {RouterLink} from "@angular/router";
import {MatTooltip} from "@angular/material/tooltip";

/**
 * The PageNotFoundComponent class represents the component used for displaying the "Not Found" page.
 *
 * @class
 * @constructor
 *
 */
@Component({
  selector: 'app-page-not-found',
  standalone: true,
  imports: [
    MatButton,
    RouterLink,
    MatTooltip
  ],
  templateUrl: './page-not-found.component.html',
  styleUrl: './page-not-found.component.scss'
})
export class PageNotFoundComponent {

}
