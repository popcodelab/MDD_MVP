import {Component, OnDestroy, OnInit} from '@angular/core';
import {MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardTitle} from "@angular/material/card";
import {MatButton} from "@angular/material/button";
import {NgForOf} from "@angular/common";
import {Subscription} from "rxjs";
import {User} from "../../../auth/interfaces/User";
import {Topic} from "../../interfaces/topic";
import {SessionService} from "../../../auth/services/session.service";
import {UserService} from "../../../auth/services/api/user.service";
import {TopicService} from "../../services/topic.service";

@Component({
  selector: 'app-topics',
  standalone: true,
  imports: [
    MatCard,
    MatCardHeader,
    MatCardContent,
    MatCardActions,
    MatCardTitle,
    MatButton,
    NgForOf
  ],
  templateUrl: './topics.component.html',
  styleUrl: './topics.component.scss'
})
export class TopicsComponent implements OnInit, OnDestroy {

  /**
   * Represents the subscription status of a user to the userService.
   */
  private userServiceSubscription: Subscription | null = null;
  /**
   * Represents the subscription status of the topic service.
   */
  private topicServiceSubscription: Subscription | null = null;

  /**
   * Represents an array of topics.
   */
  topics: Topic[] = [];
  /**
   * Array containing the subscription IDs of the user's topic subscriptions.
   */
  userTopicSubscriptionIds: number[] = [];

  /**
   * Constructor for the MyClass class.
   *
   * @param {SessionService} sessionService - The session service.
   * @param {UserService} userService - The user service.
   * @param {TopicService} topicService - The topic service.
   */
  constructor(
    private sessionService: SessionService,
    private userService: UserService,
    private topicService: TopicService
  ) {
  }

  /**
   * Checks if the user has subscribed to the specified topic.
   *
   * @param {number} topicId - The ID of the topic to check subscription for.
   * @return {boolean} - Returns true if the user has subscribed to the topic, false otherwise.
   */
  public hasUserSubscribed(topicId: number): boolean {
    return this.userTopicSubscriptionIds.includes(topicId);
  }

  /**
   * Handles the subscribe button click event for a given topic.
   *
   * @param {number} topicId - The ID of the topic to subscribe to.
   * @return {void}
   */
  onSubscribeClick(topicId: number): void {
    this.userServiceSubscription = this.userService.subscribeTopic(topicId)
      .subscribe((user: User) => {
        this.sessionService.updateUser(user);
      });
  }


  /**
   * Initializes the component.
   *
   * @return {void}
   */
  ngOnInit(): void {
    this.topicServiceSubscription = this.topicService.getAllTopics().subscribe((topics: Topic[]) => {
      this.topics = topics;
    });

    this.sessionService.topicSubscriptions$.subscribe((topics: Topic[]) => {
      this.userTopicSubscriptionIds = topics.map((topic: Topic) => topic.id);
    });
  }

  /**
   * Lifecycle hook that is called when a component, directive, or pipe is destroyed.
   *
   * This method is invoked when the component is about to be destroyed. It should be used to clean up any resources that the component is holding, such as unsubscribing from observ
   *ables or canceling any ongoing requests.
   *
   * @return {void} Nothing is returned by this method.
   */
  ngOnDestroy(): void {
    if (this.userServiceSubscription){
      this.userServiceSubscription.unsubscribe();
    }
    if (this.topicServiceSubscription){
      this.topicServiceSubscription.unsubscribe();
    }
  }

}
