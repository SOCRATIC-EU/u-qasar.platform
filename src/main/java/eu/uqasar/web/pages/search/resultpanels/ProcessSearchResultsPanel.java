package eu.uqasar.web.pages.search.resultpanels;

/*
 * #%L
 * U-QASAR
 * %%
 * Copyright (C) 2012 - 2015 U-QASAR Consortium
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import eu.uqasar.model.process.Process;
import eu.uqasar.service.AbstractService;
import eu.uqasar.service.ProcessService;
import eu.uqasar.web.pages.processes.ProcessAddEditPage;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.Model;

/**
 *
 *
 */
public class ProcessSearchResultsPanel extends AbstractSearchResultsPanel<Process> {

    @Inject
    ProcessService service;

    public ProcessSearchResultsPanel(String id, String searchTerm) {
        super(id, searchTerm, Process.class);
    }

    @Override
    protected void populateDataViewItem(Item<Process> item) {
        final Process entity = item.getModelObject();
        BookmarkablePageLink<Process> link = new BookmarkablePageLink<>("link",
                ProcessAddEditPage.class, ProcessAddEditPage.linkToEdit(entity));
        link.add(new Label("name", Model.of(entity.getName())).
                setRenderBodyOnly(true));
        item.add(link);

        item.add(new Label("description", Model.of(entity.getDescription())));
        item.add(new Label("startdate", Model.of(entity.getStartDate())));
        item.add(new Label("enddate", Model.of(entity.getEndDate())));

    }

    @Override
    protected <S extends AbstractService<Process>> S getService() {
        return (S) service;
    }

}
