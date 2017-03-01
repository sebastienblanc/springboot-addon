/**
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.aggregate;

import java.io.File;
import java.util.Arrays;

import javax.inject.Inject;

import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.context.UINavigationContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.NavigationResult;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.result.navigation.NavigationResultBuilder;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.wizard.UIWizard;

/**
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public class AggregateWizard extends AbstractProjectCommand implements UIWizard
{

   @Inject
   private ProjectFactory projectFactory;

   @Override
   protected boolean isProjectRequired()
   {
      return false;
   }

   @Override
   protected ProjectFactory getProjectFactory()
   {
      return projectFactory;
   }

   @Inject
   @WithAttributes(label = "Value", required = true)
   private UIInput<String> value;

   @Override
   public void initializeUI(UIBuilder builder) throws Exception
   {
      builder.add(value);
   }

   @Override
   public Result execute(UIExecutionContext context) throws Exception
   {
      UIContext uiContext = context.getUIContext();
      Project project = (Project) uiContext.getAttributeMap().get(Project.class);
      if (project == null) {
         project = getSelectedProject(context.getUIContext());
      }
      File folder = project.getRoot().reify(DirectoryResource.class).getUnderlyingResourceObject();
      System.out.println("Folder : " + folder.getName());
      return Results.success(value.getValue());
   }

   @Override
   public UICommandMetadata getMetadata(UIContext context)
   {
      return Metadata.from(super.getMetadata(context), getClass()).name("aggregate-wizard");
   }

   @Override
   public NavigationResult next(UINavigationContext context) throws Exception
   {
      NavigationResultBuilder builder = NavigationResultBuilder.create();
      builder.add(getMetadata(context.getUIContext()), Arrays.asList(ExampleCommand.class, ExampleTwoCommand.class));
      return builder.build();
   }

}
