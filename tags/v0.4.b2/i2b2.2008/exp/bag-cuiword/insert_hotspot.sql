/* delete hotspot instances for this experiment */
delete from hotspot_instance 
where experiment = 'bag-cuiword'
and corpus_name = 'i2b2.2008'
;

/* delete hotspot_sentences for this experiment */
delete s 
from hotspot_sentence s 
left join hotspot_instance i 
on s.hotspot_instance_id = i.hotspot_instance_id
where i.hotspot_instance_id is null
;

/*
* populate hotspot_instance
*/
insert into hotspot_instance (instance_id, label, corpus_name, experiment)
select d.docId, a.disease, 'i2b2.2008', 'bag-cuiword'
from i2b2_2008_doc d
inner join i2b2_2008_anno a 
    on a.docId = d.docId 
    and a.source = 'intuitive'
;

/*
 * get the sentences that contain hotspots
 */
insert into hotspot_sentence (hotspot_instance_id, anno_base_id, evaluation)
select h.hotspot_instance_id, ac.parent_anno_base_id, max(r.evaluation)
from hotspot_instance h
/* join hotspot_instance to hotspot via feature_eval and instance_id */
inner join hotspot ho on h.instance_id = ho.instance_id
inner join feature_rank r on r.feature_rank_id = ho.feature_rank_id
inner join feature_eval e 
    on e.feature_eval_id = r.feature_eval_id 
    and e.corpus_name = h.corpus_name
    and e.label = h.label /* must match label */
    and 
    (
        (featureset_name = 'ncuiword' and e.type = 'infogain' and param1 = '' and cv_fold_id = 0)
        or
        (e.featureset_name = '' and e.type = 'mutualinfo-child' and param1 = 'rbpar' and cv_fold_id = 0)
    )
/* get sentence for hotspot */
inner join anno_contain ac on ac.child_anno_base_id = ho.anno_base_id and ac.parent_uima_type_id = 9
where h.experiment = 'bag-cuiword'
and h.corpus_name = 'i2b2.2008'
/* testing
and h.instance_id = 2
and h.label = 'Asthma'
*/
group by h.hotspot_instance_id, ac.parent_anno_base_id;

select * from ref_uima_type;

		select distinct hi.instance_id, canonical_form
		from hotspot_instance hi
		inner join i2b2_2008_doc d 
			on hi.instance_id = d.docId 
			and d.documentSet = 'train'
		inner join i2b2_2008_disease ds 
		    on hi.label = ds.disease
		    and ds.disease_id = 1
		inner join hotspot_sentence hs
		    on hi.hotspot_instance_id = hs.hotspot_instance_id
		    and hs.evaluation >=  0.25
		/* get words in sentence */
		inner join anno_contain ac 
		    on ac.parent_anno_base_id = hs.anno_base_id
		inner join anno_word_token w
		    on w.anno_base_id = ac.child_anno_base_id
		    and canonical_form is not null
		/* exclude stopwords */
		left join stopword sw on sw.stopword = canonical_form
		/* exclude words contained in concepts */
		left join anno_contain acn
			on acn.child_anno_base_id = w.anno_base_id
			and acn.parent_uima_type_id = 8
		where hi.corpus_name = 'i2b2.2008'
		and hi.experiment = 'bag-cuiword'
		and sw.stopword is null
    and acn.parent_anno_base_id is null
;