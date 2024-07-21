import {AfterViewInit, Component, ViewChild} from "@angular/core";
import {NavbarComponent} from "../navbar/navbar.component";
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from "@angular/material/sidenav";
import {MatNavList} from "@angular/material/list";
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {MatFabButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {FooterComponent} from "../footer/footer.component";
import {NgClass} from "@angular/common";
import {MatSidenavService} from "../../core/services/mat-sidenav.service";



@Component({
  selector: 'app-content-layout',
  standalone: true,
  imports: [
    MatSidenavContent,
    RouterOutlet,
    FooterComponent,
    MatIcon,
    RouterLink,
    RouterLinkActive,
    NgClass,
    MatSidenav,
    MatSidenavContainer,
    NavbarComponent


  ],
  templateUrl: './content-layout.component.html',
  styleUrl: './content-layout.component.scss'
})
export class ContentLayoutComponent implements AfterViewInit {
  /**
   * This variable represents the title of the application.
   *
   * @type {string}
   */
  applicationTitle: String = "MDD";

  /**
   * Represents an instance of MatSidenav.
   */
  @ViewChild('matsidenav') matSidenav!: MatSidenav;

  /**
   * Constructor for creating an instance of the class.
   *
   * @param {MatSidenavService} matSidenavService - The injected service for managing the Material sidenav.
   */
  constructor(private matSidenavService: MatSidenavService) {
  }

  /**
   * Performs initialization tasks after the component's view has been fully initialized.
   * This method is called after ngAfterContentInit and ngAfterContentChecked.
   * It should be used for any initialization logic that relies on the component's view being fully rendered.
   *
   * @return {void} This method does not return any value.
   */
  ngAfterViewInit(): void {
    this.matSidenavService.setMatSidenav(this.matSidenav);
  }

  /**
   * Returns whether the Material Sidenav is opened or not.
   *
   * @return {boolean} - true if the Material Sidenav is opened, false otherwise.
   */
  get isMatSidenavOpened() {
    return this.matSidenavService.isMatSidenavOpened();
  }

  /**
   * Closes the Material Sidenav.
   *
   * @return {void}
   */
  public closeMatSidenav():void {
    this.matSidenavService.closeMatSidenav();
  }

}
