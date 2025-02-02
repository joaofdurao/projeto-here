import { Component, EventEmitter, input, output } from '@angular/core';

@Component({
  selector: 'app-qr-code-modal',
  standalone: true,
  imports: [],
  templateUrl: './qr-code-modal.component.html',
  styleUrl: './qr-code-modal.component.css'
})
export class QrCodeModalComponent {
  qrCodeUrl = input<string>()
  closeModal =  output();

  close() {
    this.closeModal.emit();
  }
}
