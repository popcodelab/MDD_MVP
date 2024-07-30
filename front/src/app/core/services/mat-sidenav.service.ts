import {Injectable} from '@angular/core';
import {MatSidenav} from "@angular/material/sidenav";

@Injectable({
  providedIn: 'root'
})
/**
 * A service for managing a Material side navigation component.
 *
 * @author Pignon Pierre-Olivier
 *
 * @version 1.0
 */
export class MatSidenavService {

  /**
   * Represents a Material side navigation component.
   * @typedef {Object} MatSidenav
   * @property {boolean} opened - Indicates whether the side navigation is currently open.
   * @property {boolean} fixedInViewport - Indicates whether the side navigation is fixed within the viewport.
   * @property {boolean} mode - Specifies the mode of the side navigation. Possible values are 'over', 'push', 'side'.
   * @property {boolean} disableClose - Indicates whether the side navigation can be closed by the user.
   * @property {boolean} openedChange - Event emitted when the opened state of the side navigation changes.
   * @property {boolean} openedStart - Event emitted when the side navigation starts opening.
   * @property {boolean} openedEnd - Event emitted when the side navigation finishes opening.
   * @property {boolean} closedStart - Event emitted when the side navigation starts closing.
   * @property {boolean} closedEnd - Event emitted when the side navigation finishes closing.
   *
   * @example
   * matSidenav.opened = true;
   * matSidenav.fixedInViewport = true;
   * matSidenav.mode = 'over';
   * matSidenav.disableClose = false;
   *
   * matSidenav.openedChange.subscribe(isOpened => {
   *    console.log(`Side navigation opened: ${isOpened}`);
   * });
   */
  private matSidenav!: MatSidenav;

  /**
   * Indicate whether the sidenav state is open or not.
   * @type {boolean}
   */
  private isOpened: boolean = false;

  /**
   * Sets the reference to the Material Sidenav.
   *
   * @param {MatSidenav} matSidenav - The Material Sidenav instance to be set.
   */
  public setMatSidenav(matSidenav: MatSidenav) {
    this.matSidenav = matSidenav;
  }

  /**
   * Checks if the sidenav is opened.
   *
   * @return {boolean} - Returns true if the sidenav is opened, otherwise false.
   */
  public isMatSidenavOpened(): boolean {
    return this.isOpened;
  }

  /**
   * Toggles the state of the Material Sidenav.
   *
   * @return {Promise<void>} - A promise that resolves when the toggling is completed.
   */
  public toggleMatSidenav() {
    this.matSidenav.toggle().then(() => {
      this.isOpened = this.matSidenav.opened;
    });
  }

  /**
   * Closes the Material Sidenav and updates the isOpened flag accordingly.
   * @return {void}
   */
  public closeMatSidenav() {
    this.matSidenav.close().then(() => {
      this.isOpened = this.matSidenav.opened;
    });
  }
}
