import { MessageService } from './../../sidebar/message.service';
import { Component, OnInit } from '@angular/core';
import { IAgentAnnouncement } from '../agent-announcement';
import { interval } from 'rxjs';

@Component({
  selector: 'app-announcement',
  templateUrl: './announcement.component.html',
  styleUrls: ['./announcement.component.scss']
})
export class AnnouncementComponent implements OnInit {

  agentAnnouncement: any;

  constructor(private messageService: MessageService) {


   }

  ngOnInit(): void {
    this.updateBoard();
      interval(4000).subscribe(()=> {
        this.messageService.getMessages().subscribe(date => {
          this.agentAnnouncement = date;
      })
    });

  }
  updateBoard(): void {
    this.messageService.getMessages().subscribe(date => {
      this.agentAnnouncement = date;
    })
  }

}
