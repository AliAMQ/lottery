/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LotteryTestModule } from '../../../test.module';
import { PrizeDeleteDialogComponent } from 'app/entities/prize/prize-delete-dialog.component';
import { PrizeService } from 'app/entities/prize/prize.service';

describe('Component Tests', () => {
    describe('Prize Management Delete Component', () => {
        let comp: PrizeDeleteDialogComponent;
        let fixture: ComponentFixture<PrizeDeleteDialogComponent>;
        let service: PrizeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LotteryTestModule],
                declarations: [PrizeDeleteDialogComponent]
            })
                .overrideTemplate(PrizeDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PrizeDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PrizeService);
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
