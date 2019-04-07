/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LotteryTestModule } from '../../../test.module';
import { PrizeDetailComponent } from 'app/entities/prize/prize-detail.component';
import { Prize } from 'app/shared/model/prize.model';

describe('Component Tests', () => {
    describe('Prize Management Detail Component', () => {
        let comp: PrizeDetailComponent;
        let fixture: ComponentFixture<PrizeDetailComponent>;
        const route = ({ data: of({ prize: new Prize(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LotteryTestModule],
                declarations: [PrizeDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PrizeDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PrizeDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.prize).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
