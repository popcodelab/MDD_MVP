import {Component} from '@angular/core';

/**
 * Represents the footer component of the application.
 *
 * @component
 * @selector app-footer
 * @templateUrl ./footer.component.html
 * @styleUrls ./footer.component.scss
 */
@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss'
})
/**
 * @author Pignon Pierre-Olivier
 *
 * @version 1.0
 */
export class FooterComponent {
  /**
   * Represents the current year.
   *
   * @type {number}
   * @description This variable stores the current year obtained using the "getUTCFullYear()" method from the JavaScript Date object.
   */
  currentYear: number = new Date().getUTCFullYear();
}
