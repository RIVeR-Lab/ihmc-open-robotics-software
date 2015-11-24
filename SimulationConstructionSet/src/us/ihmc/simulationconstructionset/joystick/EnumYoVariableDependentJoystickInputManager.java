package us.ihmc.simulationconstructionset.joystick;

import java.util.ArrayList;
import java.util.HashMap;

import us.ihmc.robotics.dataStructures.listener.VariableChangedListener;
import us.ihmc.robotics.dataStructures.variable.EnumYoVariable;
import us.ihmc.robotics.dataStructures.variable.YoVariable;
import us.ihmc.tools.inputDevices.joystick.Joystick;
import us.ihmc.tools.inputDevices.joystick.JoystickEventListener;

public class EnumYoVariableDependentJoystickInputManager<T>
{
   private final int pollIntervalMillis = 20;
   private final Joystick joystickUpdater;
   private final HashMap<Enum<?>, ArrayList<JoystickEventListener>> eventListeners = new HashMap<>();
   private final EnumYoVariable<?> enumYoVariable;
   private final T[] enumValues;
   
   public EnumYoVariableDependentJoystickInputManager(final EnumYoVariable<?> enumYoVariable, Class<T> enumType)
   {
      this.enumValues = enumType.getEnumConstants();
      this.enumYoVariable = enumYoVariable;
      
      joystickUpdater = new Joystick();
      joystickUpdater.setPollInterval(pollIntervalMillis);
      
      enumYoVariable.addVariableChangedListener(new VariableChangedListener()
      {
         @Override
         public void variableChanged(YoVariable<?> v)
         {
            updateListeners(enumYoVariable);
         }
      });
   }
   
   public Joystick getComponentSelector()
   {
      return joystickUpdater;
   }
   
   public void initialize()
   {
      updateListeners(enumYoVariable);
   }
   
   private void updateListeners(final EnumYoVariable<?> enumYoVariable)
   {
      joystickUpdater.clearEventListeners();
      T enumValue = enumValues[enumYoVariable.getOrdinal()];
      if(eventListeners.containsKey(enumValue))
      {
         for(JoystickEventListener eventListener : eventListeners.get(enumValue))
         {
            joystickUpdater.addJoystickEventListener(eventListener);
         }
      }
   }
   
   public void addJoystickMapping(EnumDependentJoystickMapping joystickMap)
   {
      eventListeners.put(joystickMap.getEnum(), joystickMap.getEventListeners());
   }
}