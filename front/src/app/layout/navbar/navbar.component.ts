import {Component} from '@angular/core';
import {MatToolbar} from "@angular/material/toolbar";
import {MatSidenavService} from "../../core/services/mat-sidenav.service";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {NgOptimizedImage} from "@angular/common";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    MatToolbar,
    MatIcon,
    MatIconButton,
    RouterLink,
    RouterLinkActive,
    NgOptimizedImage
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
/**
 * NavbarComponent is a component that represents the navigation bar of the application.
 * It provides a toggle function to control the Material Sidenav.
 *
 * @author Pignon Pierre-Olivier
 *
 * @version 1.0
 */
export class NavbarComponent {

  constructor(private matSidenavService: MatSidenavService) {
  }

  public toggleMatSidenav(): void {
    this.matSidenavService.toggleMatSidenav();
  }

}
