import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {Topic} from "../../../topics/interfaces/topic";
import {APP_CONSTANTS} from "../../../../shared/constants";
import {TopicService} from "../../../topics/services/topic.service";
import {PostService} from "../../services/post.service";
import {SnackBarService} from "../../../../core/services/snack-bar.service";
import {Post} from "../../interfaces/post";
import {NgForOf, NgIf} from "@angular/common";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption} from "@angular/material/autocomplete";
import {MatSelect} from "@angular/material/select";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-new-post',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    NgIf,
    NgForOf,
    MatError,
    MatOption,
    MatSelect,
    MatLabel,
    MatFormField,
    MatInput,
    MatButton,
    MatIcon,
    RouterLink
  ],
  templateUrl: './new-post.component.html',
  styleUrl: './new-post.component.scss'
})
export class NewPostComponent implements OnInit, OnDestroy {
  private postServiceSubscription: Subscription | null = null;
  private topicServiceSubscription: Subscription | null = null;

  topics: Topic[] = [];

  formControls: { [key: string]: FormControl } = {
    topic: new FormControl('', [Validators.required]),
    title: new FormControl('', [Validators.required]),
    content: new FormControl('', [Validators.required])
  };

  controlNames: { [key: string]: string } = {
    topic: 'topic',
    title: 'title',
    content: 'content'
  };

  labels: { [key: string]: string } = {
    topic: 'Choose a topic',
    title: 'Title',
    content: 'Write here'
  };

  errorMessages: { [key: string]: string } = {
    topic: '',
    title: '',
    content: ''
  };

  constructor(private topicService: TopicService,
              private postService: PostService,
              private snackBarService: SnackBarService) {
  }


  onSubmit(): void {
    if (this.formControls['topic'].valid && this.formControls['title'].valid && this.formControls['content'].valid) {
      const newPost: Pick<Post, 'title' | 'content' | 'topicId'> = {
        topicId: this.formControls['topic'].value,
        title: this.formControls['title'].value,
        content: this.formControls['content'].value
      };

      this.postServiceSubscription = this.postService.createPost(newPost).subscribe({
        next: () => {
          this.snackBarService.openSnackBar('Post has been created', 'Close');
          Object.values(this.formControls).forEach((control: FormControl<any>) => {
            control.reset();
            control.setErrors(null);
          });
        },
        error: error => {
          throw error;
        }
      });
    }
  }

  isFieldInvalid(fieldName: string): boolean {
    return this.formControls[fieldName].invalid && this.formControls[fieldName].touched;
  }

  isFormInvalid(): boolean {
    return this.formControls['topic'].invalid || this.formControls['title'].invalid || this.formControls['content'].invalid;
  }

  onBlur(controlName: string): void {
    const control: FormControl<any> = this.formControls[controlName];
    control.markAsTouched();
    if (controlName != 'topic') {
      this.errorMessages[controlName] = control.hasError('required') ? APP_CONSTANTS.ERROR_MESSAGES.PLEASE_ENTER
        + `${this.controlNames[controlName]}` : '';
    } else {
      this.errorMessages[controlName] = control.hasError('required') ? `Please, choose a ${this.controlNames[controlName]}` : '';
    }
  }

  ngOnInit(): void {
    this.topicServiceSubscription = this.topicService.getAllTopics().subscribe((topics: Topic[]) => {
      this.topics = topics;
    });
  }

  ngOnDestroy(): void {
    if (this.postServiceSubscription) {
      this.postServiceSubscription.unsubscribe();
    }
    if (this.topicServiceSubscription) {
      this.topicServiceSubscription.unsubscribe();
    }
  }


}
