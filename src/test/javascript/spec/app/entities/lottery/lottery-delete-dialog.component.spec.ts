/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LotteryTestModule } from '../../../test.module';
import { LotteryDeleteDialogComponent } from 'app/entities/lottery/lottery-delete-dialog.component';
import { LotteryService } from 'app/entities/lottery/lottery.service';

describe('Component Tests', () => {
    describe('Lottery Management Delete Component', () => {
        let comp: LotteryDeleteDialogComponent;
        let fixture: ComponentFixture<LotteryDeleteDialogComponent>;
        let service: LotteryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LotteryTestModule],
                declarations: [LotteryDeleteDialogComponent]
            })
                .overrideTemplate(LotteryDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(LotteryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LotteryService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
